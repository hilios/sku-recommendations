#### Input Data

Given json contains 20K articles.
Each article contains set of attributes with one set value.

#### Recommendation Engine Requirements

Calculate similarity of articles identified by sku based on their attributes values.
The number of matching attributes is the most important metric for defining similarity.
In case of a draw, attributes with name higher in alphabet (a is higher than z) is weighted with heavier weight.

**Example 1:**

```
{"sku-1": {"att-a": "a1", "att-b": "b1", "att-c": "c1"}} is more similar to
{"sku-2": {"att-a": "a2", "att-b": "b1", "att-c": "c1"}} than to
{"sku-3": {"att-a": "a1", "att-b": "b3", "att-c": "c3"}}
```

**Example 2:**

```
{"sku-1": {"att-a": "a1", "att-b": "b1"}} is more similar to 
{"sku-2": {"att-a": "a1", "att-b": "b2"}} than to
{"sku-3": {"att-a": "a2", "att-b": "b1"}}
```

#### Recommendation request example

sku-123  > ENGINE > 10 most similar skus based on criteria described above with their corresponding weights.

#### Expected delivery format
tgz file containing solution with simple instructions how to run data import and how to execute recommendation request
