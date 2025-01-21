import java.util.Objects;

class ConclusionReport {
    int playerId;
    int winOrLoss;
    int bidCount;
    int rateCondition1;
    int rateCondition2;

    public ConclusionReport(int playerId, int winOrLoss, int bidCount, int rateCondition1, int rateCondition2) {
        this.playerId = playerId;
        this.winOrLoss = winOrLoss;
        this.bidCount = bidCount;
        this.rateCondition1 = rateCondition1;
        this.rateCondition2 = rateCondition2;
    }

    @Override
    public String toString() {
        return "Player ID: " + playerId + ", Win/Loss: " + winOrLoss +
               ", Bid Count: " + bidCount + ", Rate 1: " + rateCondition1 +
               ", Rate 2: " + rateCondition2;
    }
}


class AggregationKey {
    int playerId;
    int rateCondition1;
    int rateCondition2;

    public AggregationKey(int playerId, int rateCondition1, int rateCondition2) {
        this.playerId = playerId;
        this.rateCondition1 = rateCondition1;
        this.rateCondition2 = rateCondition2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregationKey that = (AggregationKey) o;
        return playerId == that.playerId &&
               rateCondition1 == that.rateCondition1 &&
               rateCondition2 == that.rateCondition2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, rateCondition1, rateCondition2);
    }
}
