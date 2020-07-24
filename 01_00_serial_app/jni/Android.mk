LOCAL_PATH := $(call my-dir) 
  
include $(CLEAR_VARS) 
  
TARGET_PLATFORM := android-28
LOCAL_MODULE := libserial_port 
LOCAL_SRC_FILES := serialport.cpp 
LOCAL_LDLIBS := -llog 
  
include $(BUILD_SHARED_LIBRARY) 
