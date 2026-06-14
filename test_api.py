import os
from openai import OpenAI

# Khchi l-API Key dyalk direct hna blassa nvapi-...
api_key = os.getenv("NVIDIA_API_KEY") or "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"

client = OpenAI(
    base_url="https://integrate.api.nvidia.com/v1",
    api_key=api_key
)

try:
    completion = client.chat.completions.create(
        model="deepseek-ai/deepseek-v4-pro",
        messages=[{"role": "user", "content": "Reply with only: OK"}],
        max_tokens=10
    )
    print("Natija mn l-API:", completion.choices[0].message.content)
except Exception as e:
    print("Kayn Error:", e)
