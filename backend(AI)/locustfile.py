from locust import HttpUser, task, between

class ServerTest(HttpUser):
    wait_time = between(1, 2)
    
    @task
    def get_user_recommed(self):
        user_id = 1
        self.client.get(f'/home/{user_id}')

    @task
    def get_user_recommed_artwork(self):
        user_id = 1
        self.client.get(f'/home/artwork/{user_id}')

    @task
    def get_portfolio(self):
        user_id = 1
        self.client.get(f'/portfolio/{user_id}')