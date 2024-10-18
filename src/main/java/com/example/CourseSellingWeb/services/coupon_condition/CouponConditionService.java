package com.example.CourseSellingWeb.services.coupon_condition;

import com.example.CourseSellingWeb.dtos.CouponConditionDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Coupon;
import com.example.CourseSellingWeb.models.CouponCondition;
import com.example.CourseSellingWeb.respositories.CouponConditionRepository;
import com.example.CourseSellingWeb.services.coupon.CouponServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponConditionService implements CouponConditionServiceImpl{
    private final CouponConditionRepository couponConditionRepository;
    private final CouponServiceImpl couponService;

    @Override
    public CouponCondition create(CouponConditionDTO couponConditionDTO) throws DataNotFoundException {
        Coupon existsCoupon = couponService.getById(couponConditionDTO.getCouponId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + couponConditionDTO.getCouponId()));
        CouponCondition newCouponCondition = CouponCondition.builder()
                    .coupon(existsCoupon)
                    .discountValue(couponConditionDTO.getDiscountValue())
                    .expirationDate(couponConditionDTO.getExpirationDate())
                    .minimumOrderValue(couponConditionDTO.getMinimumOrderValue())
                    .build();
            couponConditionRepository.save(newCouponCondition);

        return newCouponCondition;
    }

    @Override
    public CouponCondition update(int id, CouponConditionDTO couponConditionDTO) throws DataNotFoundException {
        Coupon existsCoupon = couponService.getById((couponConditionDTO.getCouponId()))
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + couponConditionDTO.getCouponId()));
        CouponCondition existsCouponCondition = couponConditionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));

        existsCouponCondition.setCoupon(existsCoupon);
        existsCouponCondition.setDiscountValue(couponConditionDTO.getDiscountValue());
        existsCouponCondition.setExpirationDate(couponConditionDTO.getExpirationDate());
        existsCouponCondition.setMinimumOrderValue(couponConditionDTO.getMinimumOrderValue());

        couponConditionRepository.save(existsCouponCondition);

        return existsCouponCondition;
    }

    @Override
    public Optional<CouponCondition> getById(int id) throws DataNotFoundException {
        CouponCondition existsCouponCondition = couponConditionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        return Optional.ofNullable(existsCouponCondition);
    }

    @Override
    public Optional<CouponCondition> getByCouponId(int couponId) throws DataNotFoundException {
        CouponCondition existsCouponCondition = couponConditionRepository.getByCourseId(couponId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + couponId));
        return Optional.ofNullable(existsCouponCondition);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Optional<CouponCondition> optionalCouponCondition = couponConditionRepository.findById(id);
        optionalCouponCondition.ifPresent(couponConditionRepository :: delete);
    }
}
