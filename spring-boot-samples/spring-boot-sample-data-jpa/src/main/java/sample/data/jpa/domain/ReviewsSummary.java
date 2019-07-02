

package sample.data.jpa.domain;

import sample.data.jpa.domain.Rating;

public interface ReviewsSummary {

	long getNumberOfReviewsWithRating(Rating rating);

}
