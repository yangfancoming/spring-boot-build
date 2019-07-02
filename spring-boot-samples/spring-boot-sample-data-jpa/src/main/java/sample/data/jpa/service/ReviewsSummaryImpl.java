package sample.data.jpa.service;

import sample.data.jpa.domain.Rating;
import sample.data.jpa.domain.RatingCount;
import sample.data.jpa.domain.ReviewsSummary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 64274 on 2019/7/2.
 *
 * @ Description: TODO
 * @ author  山羊来了
 * @ date 2019/7/2---9:36
 */
public class ReviewsSummaryImpl implements ReviewsSummary {

	private final Map<Rating, Long> ratingCount;

	ReviewsSummaryImpl(List<RatingCount> ratingCounts) {
		this.ratingCount = new HashMap<>();
		for (RatingCount ratingCount : ratingCounts) {
			this.ratingCount.put(ratingCount.getRating(), ratingCount.getCount());
		}
	}

	@Override
	public long getNumberOfReviewsWithRating(Rating rating) {
		Long count = this.ratingCount.get(rating);
		return (count != null) ? count : 0;
	}

}