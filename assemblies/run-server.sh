#!/bin/sh

#Where user can create and overload the agents/seds configuration files
TEMPLATE_DIRECTORY_PATH=`pwd`/templates
echo "Template directory is : " $TEMPLATE_DIRECTORY_PATH
java -classpath classes:lib/* com.sysfera.godiet.core.GodietServerMain