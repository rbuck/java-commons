# Description

This project provides utility classes for Java development that I have open-sourced
under the Apache 2.0 License to the Java community. There are more utility classes
than these, but the ones open sourced are used so often that it was believed others
may find them useful too.

# Build Procedure

To compile and test the project issue the following commands:

    mvn clean install

To release the project issue the following commands:

    mvn clean release:prepare -Dgpg.passphrase= -Dgpg.keyname=

# License

See the LICENSE file herein.
