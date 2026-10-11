from app.graphql.schema import schema

def test_graphql_ping_query():
    query = "{ ping }"
    result = schema.execute_sync(query)
    assert result.errors is None
    assert "MkulimaAI Backend GraphQL API online" in result.data["ping"]

def test_graphql_market_prices():
    query = """
    {
      allMarketPrices {
        cropName
        marketLocation
        pricePerUnit
      }
    }
    """
    result = schema.execute_sync(query)
    assert result.errors is None
    assert len(result.data["allMarketPrices"]) > 0
