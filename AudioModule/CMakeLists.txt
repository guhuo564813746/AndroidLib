
cmake_minimum_required(VERSION 3.4.1)
add_subdirectory(${CMAKE_SOURCE_DIR}/src/main/cpp/soundtouch)

add_library( ffmpeg_deal
             SHARED
             src/main/cpp/ffmpeg_deal.cpp )

find_library( log-lib
              log )


include_directories(libs/include)
set(DIR ../../../../libs)
add_library(avcodec-56
            SHARED
            IMPORTED)
set_target_properties(avcodec-56
                      PROPERTIES IMPORTED_LOCATION
                      ${DIR}/arm64-v8a/libavcodec-56.so)

add_library(avdevice-56
            SHARED
            IMPORTED)
set_target_properties(avdevice-56
                      PROPERTIES IMPORTED_LOCATION
                      ${DIR}/arm64-v8a/libavdevice-56.so)
add_library(avformat-56
            SHARED
            IMPORTED)
set_target_properties(avformat-56
                      PROPERTIES IMPORTED_LOCATION
                      ${DIR}/arm64-v8a/libavformat-56.so)
add_library(avutil-54
            SHARED
            IMPORTED)
set_target_properties(avutil-54
                      PROPERTIES IMPORTED_LOCATION
                      ${DIR}/arm64-v8a/libavutil-54.so)
add_library(postproc-53
            SHARED
            IMPORTED)
set_target_properties(postproc-53
                      PROPERTIES IMPORTED_LOCATION
                      ${DIR}/arm64-v8a/libpostproc-53.so)
add_library(swresample-1
             SHARED
             IMPORTED)
set_target_properties(swresample-1
                      PROPERTIES IMPORTED_LOCATION
                      ${DIR}/arm64-v8a/libswresample-1.so)
  add_library(swscale-3
              SHARED
              IMPORTED)
  set_target_properties(swscale-3
                        PROPERTIES IMPORTED_LOCATION
                        ${DIR}/arm64-v8a/libswscale-3.so)
  add_library(avfilter-5
              SHARED
              IMPORTED)
  set_target_properties(avfilter-5
                        PROPERTIES IMPORTED_LOCATION
                        ${DIR}/arm64-v8a/libavfilter-5.so)
target_link_libraries( ffmpeg_deal
                       avfilter-5
                       avcodec-56
                       avdevice-56
                       avformat-56
                       avutil-54
                       postproc-53
                       swresample-1
                       swscale-3
                       ${log-lib}
                       android)