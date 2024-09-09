from typing import Union
from fastapi import FastAPI
from recommend import urls as rec_url
from generation import urls as gen_url

app = FastAPI()

app.include_router(rec_url.router)
app.include_router(gen_url.router)



if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", reload=True)