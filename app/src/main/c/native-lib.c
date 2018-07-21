#include <string.h>
#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_project3s1_MainActivity_stringFromJNI(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "Hello from the other side!!!");
}
