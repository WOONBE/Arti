import json
import os
from urllib.parse import quote_plus

from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

SECRET_FILE = os.path.join(BASE_DIR, 'secrets.json')
# SECRET_FILE = os.path.join(BASE_DIR, 'secrets_mariadb.json')
secrets = json.loads(open(SECRET_FILE).read())
db = secrets["DB"]

# SQLALCHEMY_DATABASE_URL = f"mysql+pymysql://{db.get('user')}@stg-yswa-kr-practice-db-master:{db.get('password')}@{db.get('host')}:{db.get('port')}/{db.get('database')}?charset=utf8"
SQLALCHEMY_DATABASE_URL = f"mysql+pymysql://{db.get('user')}:{db.get('password')}@{db.get('host')}:{db.get('port')}/{db.get('database')}"


engine = create_engine(
    SQLALCHEMY_DATABASE_URL,
    pool_recycle=28000,  # 28000초마다 연결을 재활용
    pool_pre_ping=True   # MySQL 연결 확인
    )

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()