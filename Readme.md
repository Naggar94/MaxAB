# Flairstech Java Workshop

This is the technical java workshop submitted upon your request as sent in the mail.

Here I'll Discuss What Have I Done, And how to run this project.


# Database

Database image must be downloaded during build and connected to during **build!** and connect to it during Runtime.

So I've created a shell script to clone the Database image from the repository and start the docker container.

Then maven execution plugin is responsible of executing this shell script. the script is located in **scripts/**

You have to change **$folder_path** variable in the shell script to the desired location where the database will be cloned.

# Unit Testing

I've created Three test scenarios one valid scenario with a correct key, and another one with incorrect one, both are working properly. Also I've created the test which is responsible of testing the server response when the DB is down. It's the commented one.

# Start your trial

Now you want to start your tour, OK. So you have to clone this repo on your local machine.

For me I'm using eclipse, but you should have at least maven installed on your device in order to get the build target jar file to attach it to the docker container. Just write the following in the root directory of the repository.

    mvn clean install

Then in the root of the directory type the following:

    docker build -f Dockerfile -t docker-spring-boot .

**docker-spring-boot** is a name which I've used to build this spring boot project. You could change it to whatever you want. **JUST REMEMBER IT CAUSE YOU'LL NEED IT IN THE NEXT STEP.**

After building the image, run the following command:

    docker run -d -p 8085:8085 docker-spring-boot
Here I'm mapping port 8085 in my machine "The one on L.H.S." to the port of the project in the docker container "The one on R.H.S." you could change the "L.H.S." directly from the command line as desired, but if you want to change the project port listener just navigate to the **application.properties** in the project and change the following line:

    server.port=8085

That's all for now.
Thanks.