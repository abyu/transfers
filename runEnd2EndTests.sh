./gradlew clean build
java -jar build/libs/transfers.jar > output.log &
PID=$!
echo "Running app at port 8080 with PID $PID"
echo "Waiting for 5 Seconds for app to startup"
sleep 5s
./gradlew end2endTest
kill $PID