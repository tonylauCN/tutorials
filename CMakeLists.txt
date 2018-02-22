cmake_minimum_required(VERSION 3.9)
project(tutorials C)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=gnu99 -Wall -Werror -Wunused-variable")
set(CMAKE_C_FLAGS_RELEASE "") # removes -NDEBUG
set(CMAKE_BUILD_TYPE Debug)

set(SRC_PATH ${CMAKE_CURRENT_SOURCE_DIR}/lua/CAPI)

add_executable(demo ${SRC_PATH}/sample.c)

#target_link_libraries(tutorials lua)
