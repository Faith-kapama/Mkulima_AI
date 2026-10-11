import numpy as np

def normalize_price_history(prices: list) -> np.ndarray:
    arr = np.array(prices, dtype=np.float32)
    if arr.max() == arr.min():
        return np.zeros_like(arr)
    return (arr - arr.min()) / (arr.max() - arr.min())
