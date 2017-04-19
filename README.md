# Recommendations Engine

A simple recommendation engine based in a weighted attributes set.

#### Running

Execute the `jar` in your environment passing the path of the input JSON data as argument, then type the SKU you would like to fetch recommendations: 

```shell
$ java -jar recommendations.jar ./test-data.json
Press Ctrl+C to exit
Type a SKU: sku-5

+-----------------+---------+
| SKU             | Rank    |
+-----------------+---------+
| sku-16083       | 6.95508 |
| sku-74          | 5.71973 |
| sku-5926        | 5.67578 |
| sku-13272       | 5.64551 |
| sku-1668        | 5.59961 |
| sku-18233       | 5.47656 |
| sku-19782       | 5.15430 |
| sku-8979        | 5.05957 |
| sku-5784        | 4.93750 |
| sku-12905       | 4.89063 |
+-----------------+---------+

Type a SKU:
```

#### Development

From the source code root execute with SBT tool:

```shell
$ sbt "run ./src/main/resources/test-data.json"
```

## Ranking strategy

The algorithm scores the SKU using the following formula: 

1. Each attribute match has weight 1
2. Weight the match based in the position of the attribute using a exponential survival function.

The first attributes (a, b, c, etc.) has heavier weight in the comparision to the latter, but never the same weight as the attribute match, because the survival function is an asymptote curve with maximum cumulative sum of one.

## Design considerations

This code aims to KISS and use as few external dependencies as possibles, after parsing it holds all data in memory allowing fast access to the request function given the test data is small (< 500Mb). 

There are some drawbacks caused by this approach that could be optimized in future versions if the input grows:

- Parse json as a stream;
- Use a columnar fast access pre-processed file (ie: Parquet);
- Store values or ranking into a key-based database;

## The problem

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
