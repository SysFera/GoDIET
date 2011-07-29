#!/bin/sh

#Where user can create and overload the agents/seds configuration files
export TEMPLATE_DIRECTORY_PATH=`pwd`/templates
echo "Template directory is : " $TEMPLATE_DIRECTORY_PATH
#Where user can add his referenced script
export COPY_DIRECTORY_PATH=`pwd`/copy
echo "Copy directory is : " $COPY_DIRECTORY_PATH
java -classpath classes:lib/* com.sysfera.godiet.core.GodietServerMain
