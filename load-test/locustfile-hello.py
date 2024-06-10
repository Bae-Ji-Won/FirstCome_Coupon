# 부하테스트를 위한 외부 api 설정
from locust import task, FastHttpUser

class HelloWorld(FastHttpUser):
    connection_timeout = 10.0
    network_tineout = 10.0

    @task
    def hello(self):
        self.client.get("/hello")
