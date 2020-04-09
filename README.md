# Skaffold Test

This is a kubernetes / mysql vertx client test project

## Requirements
- Java 8 SDK installed
- Skaffold v1.7.0
- Kubernetes (Docker desktop on Mac) 2.2.0.5
- Gradle 6.3

## How to run
1. Clone this repository
1. Run `skaffold dev`
1. Once all services up, you are supposed to be able to access 
    ```
    http://localhost:8080/hello
    ``` 
1. It'll render `Hello World` in the browser.
1. Hot Deploy Demonstration
    
    Open `HelloController.java` and change `Hello World` to `Hello New World`
    
    Then run `./gradlew clean assemble` and confirm it's successfully compiled.
    
    `skaffold` would detect the change of artifact and would automatically deploy to the Kubernetes cluster
    
    Once the deployment is done, access to `http://localhost:8080/hello` again. You'll see the `Hello New World` message in the browser.    

## How to debug with IDE (e.g. IntelliJ, Eclipse)
1. Run `skaffold dev`
1. Once confirmed all services up, run `make fwd pod={your pod}`. 
    
    For example, Run 
    ```
    kubectl get -n dev pod | grep mn-test`
    ```
    then use the `NAME` as below.
    ```
    make fwd pod=mn-test-574f696b46-kd729
    ```
1. Now you can connect to the pod via port `5005`. Please connect to the port with Remote debug functionality of your IDE.

