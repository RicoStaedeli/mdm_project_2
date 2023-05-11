# MDM Project 2 -- Object Detection with DJL
In diesem Projekt wurde mittels eines Models von JDL eine Spring Boot Applikation entwickelt, welche in der Lage ist Objekte in einem Bild zu erkenne. 

## Docker Image Builden und auf Dockerhub pushen
docker build -t name/project2:0.0.1 .
docker push name/project2:0.0.1

## Deployment auf Azure
Als erstes muss eine neue Gruppe erstellt werden. Anschliessend kann der Container erstellt werden. 

### Gruppe erstellen
az login
az group create --location switzerlandnorth --name mdm
az acr create --resource-group mdm --name zhawregistry --sku Basic --admin-enabled true

### Deployment
az container delete -g mdm --yes --name project2docker 
az container create -g mdm --name project2docker --image name/project2:0.0.1 --ports 8080 --restart-policy Always --ip-address Public