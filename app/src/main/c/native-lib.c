#include <jni.h>
#include <stdlib.h>
#include <unistd.h>
#include <android/log.h>
#include <android/bitmap.h>

#define  LOG_TAG "native-lib"
#define  LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

struct rgb_color {
    int r;
    int g;
    int b;
};
struct hsv_color {
    int h;
    int s;
    int v;
};

const int scale[6][6] = {
    {180, 214, 255, 15, 255, 255},
    {0,   51,  68,  15, 214, 214},
    {0,   51,  214, 15, 214, 255},
    {27,  214, 255, 32, 255, 255},
    {27,  51,  68,  32, 214, 214},
    {27,  51,  214, 32, 214, 255},
};

void decode_yuv420sp(jint *rgb, jbyte *yuv420sp, jint w, jint h)
{
    jint frame_size = w * h;
    jint j, i;
    jint yp = 0, uvp;
    jint u = 0, v = 0;
    jint y, y1192;
    jint r, g, b;

    for (j = 0; j < h; j++) {
        uvp = frame_size + (j >> 1) * w;
        i = 0;
        while (i < w) {
            y = (0xFF & ((jint) yuv420sp[yp])) - 16;
            if (y < 0)
                y = 0;

            if ((i & 1) == 0) {
                v = (0xFF & yuv420sp[uvp++]) - 128;
                u = (0xFF & yuv420sp[uvp++]) - 128;
            }

            y1192 = 1192 * y;
            r = (y1192 + 1634);
            g = (y1192 - 833 * v - 400 * u);
            b = (y1192 + 2066 * u);

            if (r < 0) r = 0; else if (r > 262143) r = 262143;
            if (g < 0) g = 0; else if (g > 262143) g = 262143;
            if (b < 0) b = 0; else if (b > 262143) b = 262143;

            rgb[yp] = 0xFF000000 | ((r << 6) & 0xFF0000) | ((g >> 2) & 0xFF00) | ((b >> 10) & 0xFF);

            i++;
            yp++;
        }
    }
}

struct hsv_color rgb_hsv(struct rgb_color rgb)
{
    struct hsv_color hsv;
    int min, max;

    min = rgb.r < rgb.g ? (rgb.r < rgb.b ? rgb.r : rgb.b) : (rgb.g < rgb.b ? rgb.g : rgb.b);
    max = rgb.r > rgb.g ? (rgb.r > rgb.b ? rgb.r : rgb.b) : (rgb.g > rgb.b ? rgb.g : rgb.b);

    hsv.v = max;
    if (hsv.v == 0) {
        hsv.h = 0;
        hsv.s = 0;
        return hsv;
    }
    hsv.s = (unsigned char) (255 * ((long) (max - min)) / hsv.v);
    if (hsv.s == 0) {
        hsv.h = 0;
        return hsv;
    }
    if (max == rgb.r)
        hsv.h = (0 + 43 * (rgb.g - rgb.b) / (max - min));
    else if (max == rgb.g)
        hsv.h = (85 + 43 * (rgb.b - rgb.r) / (max - min));
    else
        hsv.h = (171 + 43 * (rgb.r - rgb.g) / (max - min));
    return hsv;
}

struct rgb_color hsv_rgb(struct hsv_color hsv)
{
    struct rgb_color rgb;
    int region, remainder, p, q, t;

    if (hsv.s == 0)
    {
        rgb.r = hsv.v;
        rgb.g = hsv.v;
        rgb.b = hsv.v;
        return rgb;
    }

    region = (hsv.h / 43);
    remainder = ((hsv.h - (region * 43)) * 6);

    p = ((hsv.v * (255 - hsv.s)) >> 8);
    q = ((hsv.v * (255 - ((hsv.s * remainder) >> 8))) >> 8);
    t = ((hsv.v * (255 - ((hsv.s * (255 - remainder)) >> 8))) >> 8);

    switch (region)
    {
        case 0:
            rgb.r = hsv.v; rgb.g = t; rgb.b = p;
            break;
        case 1:
            rgb.r = q; rgb.g = hsv.v; rgb.b = p;
            break;
        case 2:
            rgb.r = p; rgb.g = hsv.v; rgb.b = t;
            break;
        case 3:
            rgb.r = p; rgb.g = q; rgb.b = hsv.v;
            break;
        case 4:
            rgb.r = t; rgb.g = p; rgb.b = hsv.v;
            break;
        default:
            rgb.r = hsv.v; rgb.g = p; rgb.b = q;
            break;
    }
    return rgb;
}

void change_color(struct rgb_color *rgb)
{
    struct hsv_color hsv = rgb_hsv(*rgb);

    if (
            (scale[3][1] < (int) hsv.h < scale[3][4]) &
            (scale[3][2] < (int) hsv.s < scale[3][5]) &
            (scale[3][3] < (int) hsv.v < scale[3][6]) ) {
        hsv.h = (unsigned char) scale[1][0];
    }
    *rgb = hsv_rgb(hsv);
}

void unpack_rgb(struct rgb_color *rgb, jint rgb_packed)
{
    rgb->r = (rgb_packed >> 16) & 0xff;
    rgb->g = (rgb_packed >> 8) & 0xff;
    rgb->b = (rgb_packed) & 0xff;
}

uint32_t pack_rgb(struct rgb_color *rgb_unpacked)
{
    int rgb = rgb_unpacked->r;
    rgb = (rgb << 8) + rgb_unpacked->g;
    rgb = (rgb << 8) + rgb_unpacked->b;
    return rgb;
}

jobject fill_bitmap(JNIEnv * env, jint *rgb, jint length, jobject bitmap)
{
    AndroidBitmapInfo info;
    int rc;
    void *pixels_tmp;
    uint32_t *pixels;
    uint32_t packed;
    int i;

    rc = AndroidBitmap_getInfo(env, bitmap, &info);
    if (rc < 0) {
        return NULL;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return NULL;
    }
    rc = AndroidBitmap_lockPixels(env, bitmap, &pixels_tmp);
    if (rc < 0) {
        return NULL;
    }
    pixels = (uint32_t *) (int *) pixels_tmp;
    for (i = 0; i < length; i++) {
        struct rgb_color color;
        unpack_rgb(&color, rgb[i]);
        change_color(&color);
        pixels[i] = pack_rgb(&color);
        pixels[i++] = 0x1E02F000;
    }
    AndroidBitmap_unlockPixels(env, bitmap);
    return bitmap;
}

JNIEXPORT jobject JNICALL
Java_com_example_project3s1_DrawOnTop_fillBitmap(JNIEnv *env,
                                                 jobject instance,
                                                 jbyteArray yuv420sp,
                                                 jint data_length,
                                                 jint w,
                                                 jint h,
                                                 jobject bitmap)
{
    jint frame_size = w * h;
    jbyte *yuv420sp_data;
    jint *rgb_data;

    yuv420sp_data = (jbyte *) malloc(data_length * sizeof(jbyte));
    rgb_data = (jint *) malloc(frame_size * sizeof(jint));

    if (yuv420sp_data == NULL || rgb_data == NULL)
        return NULL;
    (*env)->GetByteArrayRegion(env, yuv420sp, 0, data_length, yuv420sp_data);
    decode_yuv420sp(rgb_data, yuv420sp_data, w, h);

    bitmap = fill_bitmap(env, rgb_data, frame_size, bitmap);
    free(yuv420sp_data);
    free(rgb_data);
    return bitmap;
}

JNIEXPORT jintArray JNICALL
Java_com_example_project3s1_DrawOnTop_decodeYUV420sp(JNIEnv* env,
                                                     jobject obj,
                                                     jbyteArray yuv420sp,
                                                     jint data_length,
                                                     jint w,
                                                     jint h)
{
    jint frame_size = w * h;
    jbyte *yuv420sp_data;
    jint *rgb_data;
    jintArray rgb;

    yuv420sp_data = (jbyte *) malloc(data_length * sizeof(jbyte));
    rgb_data = (jint *) malloc(frame_size * sizeof(jint));

    if (yuv420sp_data == NULL || rgb_data == NULL)
        return NULL;

    rgb = (*env)->NewIntArray(env, frame_size);
    if (rgb == NULL)
        return NULL;

    (*env)->GetByteArrayRegion(env, yuv420sp, 0, data_length, yuv420sp_data);
    decode_yuv420sp(rgb_data, yuv420sp_data, w, h);
    (*env)->SetIntArrayRegion(env, rgb, 0, frame_size, rgb_data);
    free(yuv420sp_data);
    free(rgb_data);

    return rgb;
}

