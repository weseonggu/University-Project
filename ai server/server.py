from fastapi import FastAPI, Request
from pydantic import BaseModel
import torch
from transformers import PreTrainedTokenizerFast, GPT2LMHeadModel, AutoModelForCausalLM, AutoTokenizer, pipeline
import accelerate
import uvicorn

app = FastAPI()

# 배달 모델
loaded_hugging_model = GPT2LMHeadModel.from_pretrained("mogoi/test_de_fin")

loaded_hugging_tokenizer = PreTrainedTokenizerFast.from_pretrained("mogoi/test_de_fin")

# 예약 모델
load_reservation_model = GPT2LMHeadModel.from_pretrained("mogoi/kogpt2_reservation")

load_reservation_tokenizer = PreTrainedTokenizerFast.from_pretrained("mogoi/kogpt2_reservation")



class Conversation(BaseModel):
    sentences: str

class ChatResponse(BaseModel):
    response: str


def clean_generated_text(text):

    if "[/INST]" in text:
        text = text.split("[/INST]")[1].strip()
    if "손님:" in text:
        text = text.replace("손님:", "").strip()
    if "직원:" in text:
        text = text.replace("직원:", "").strip()

    return text


@app.get("/isConnected")
async def chat_endpoint():

    return "연결되었습니다."

@app.post("/delivery-chat")
async def chat_endpoint(chat_request: Conversation):
    prompt = chat_request.sentences
    pipe = pipeline(task="text-generation", model=loaded_hugging_model, tokenizer=loaded_hugging_tokenizer, max_length=200)
    result = pipe(f"<s>[INST] {prompt} [/INST]")
    cleaned_text = clean_generated_text(result[0]['generated_text'])
    return cleaned_text

@app.post("/reservation-chat")
async def reservation_endpoint(chat_request: Conversation):
    prompt = chat_request.sentences
    pipe = pipeline(task="text-generation", model=load_reservation_model, tokenizer=load_reservation_tokenizer, max_length=200)
    result = pipe(f"<s>[INST] {prompt} [/INST]")
    cleaned_text = clean_generated_text(result[0]['generated_text'])
    return cleaned_text