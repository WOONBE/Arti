from boto3 import client
import io
import os
import json

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

SECRET_FILE = os.path.join(BASE_DIR, 'secrets.json')
# SECRET_FILE = os.path.join(BASE_DIR, 'secrets_mariadb.json')
secrets = json.loads(open(SECRET_FILE).read())
s3 = secrets["S3"]


s3_client = client(
    "s3",
    aws_access_key_id = s3.get('aws_access_key_id'), # 본인 소유의 키를 입력
    aws_secret_access_key = s3.get('aws_secret_access_key'), # 본인 소유의 키를 입력
    region_name = s3.get('region_name')
)

def upload_to_s3(file: io.BytesIO, bucket_name: str, file_name: str) -> None:
    s3_client.upload_fileobj(
        file,
        bucket_name,
        file_name,
        ExtraArgs={"ContentType": "image/jpeg", "ACL": "public-read"},
    )