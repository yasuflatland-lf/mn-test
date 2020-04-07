apps-build:
	./gradlew clean assemble

docker-build-dev:
	docker build --force-rm -t localhost:5000/mn-test ./

docker-push-dev:
	docker push localhost:5000/mn-test:latest

dp-dev: apps-build docker-build-dev docker-push-dev

fwd:
	kubectl port-forward -n dev ${pod} 5005:5005

run-registry:
	docker run -i -t -d --rm -p=5000:5000 --name="registry" registry:2.6

clean-registry:
	docker container stop registry