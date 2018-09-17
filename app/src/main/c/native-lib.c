#include <jni.h>

JNIEXPORT jint JNICALL
Java_com_example_project3s1_Preview_decodeYUV420sp(JNIEnv* env,
                                                     jobject obj,
                                                     jbyteArray yuv420sp)
{
    jbyte tmp[10];
    jint i, sum = 0;
    (*env)->GetByteArrayRegion(env, yuv420sp, 0, 10, tmp);
    for (i = 0; i < 10; i++)
        sum += tmp[i];
    return sum;
}
