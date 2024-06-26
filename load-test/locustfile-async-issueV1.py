# 부하테스트를 위한 외부 api 설정
import random
from locust import task, FastHttpUser

class CouponIssueV1(FastHttpUser):
    connection_timeout = 10.0
    network_tineout = 10.0

    @task
    def issue(self):
        payload={
            "userId": random.randint(1,10000000),
            "couponId": 1,
        }
        with self.rest("POST","/v1/issue-async",json=payload):
            pass
