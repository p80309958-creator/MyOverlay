#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_myoverlay_MainActivity_getMessage(
        JNIEnv* env,
        jobject) {
    return env->NewStringUTF("Hello from C++!");
}
