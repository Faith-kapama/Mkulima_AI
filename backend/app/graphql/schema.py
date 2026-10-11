import strawberry
from app.graphql.queries.user_queries import UserQueries
from app.graphql.queries.crop_queries import CropQueries
from app.graphql.queries.disease_queries import DiseaseQueries
from app.graphql.queries.market_queries import MarketQueries
from app.graphql.queries.marketplace_queries import MarketplaceQueries

from app.graphql.mutations.auth_mutations import AuthMutations
from app.graphql.mutations.diagnosis_mutations import DiagnosisMutations
from app.graphql.mutations.user_mutations import UserMutations
from app.graphql.mutations.marketplace_mutations import MarketplaceMutations
from app.schemas.market import YieldPredictionPayload
from app.ml.yield_prediction.predictor import YieldPredictor

yield_predictor = YieldPredictor()

@strawberry.type
class Query(
    UserQueries,
    CropQueries,
    DiseaseQueries,
    MarketQueries,
    MarketplaceQueries
):
    @strawberry.field
    def ping(self) -> str:
        return "MkulimaAI Backend GraphQL API online - Karibu!"

@strawberry.type
class Mutation(
    AuthMutations,
    DiagnosisMutations,
    UserMutations,
    MarketplaceMutations
):
    @strawberry.mutation
    def predict_crop_yield(
        self,
        crop: str,
        acres: float,
        soil_type: str,
        rainfall: float = 750.0,
        fertilizer_used: bool = True
    ) -> YieldPredictionPayload:
        pred = yield_predictor.predict(
            crop=crop,
            acres=acres,
            soil_type=soil_type,
            rainfall=rainfall,
            fertilizer_used=fertilizer_used
        )
        return YieldPredictionPayload(
            success=True,
            predicted_bags=pred["predicted_bags"],
            predicted_tons=pred["predicted_tons"],
            confidence_percent=pred["confidence_percent"],
            optimal_harvest_month=pred["optimal_harvest_month"],
            recommendations=pred["recommendations"]
        )

schema = strawberry.Schema(query=Query, mutation=Mutation)
