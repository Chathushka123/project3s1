#include <jni.h>
#include <stdlib.h>
#include <unistd.h>
#include <android/log.h>
#include <android/bitmap.h>

#define  LOG_TAG "native-lib"
#define  LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)


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

jobject fill_bitmap(JNIEnv * env, jint *rgb, jint length, jobject bitmap)
{
    AndroidBitmapInfo info;
    int rc;
    void *pixels_tmp;
    uint32_t *pixels;
    jint i;

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
    pixels = (uint32_t *) pixels_tmp;
    for (i = 0; i < length; i++) {
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

