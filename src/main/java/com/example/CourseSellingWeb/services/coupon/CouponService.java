package com.example.CourseSellingWeb.services.coupon;

import com.example.CourseSellingWeb.dtos.CouponDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Coupon;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.respositories.CouponRepository;
import com.example.CourseSellingWeb.services.employee.EmployeeServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService implements CouponServiceImpl{
    private  final CouponRepository couponRepository;
    private  final EmployeeServiceImpl employeeService;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    @Override
    public String generateCoupon() {
        int length = 8;
        StringBuilder coupon = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            coupon.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return coupon.toString();
    }
    @Override
    public Coupon create(CouponDTO couponDTO) throws DataNotFoundException {
        Employee existsEmployee = employeeService.getEmployeeById(couponDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + couponDTO.getEmployeeId()));
        boolean existsCode = couponRepository.existsByCode(couponDTO.getCode());
        Coupon newCoupon = new Coupon();
        if(!existsCode){
            newCoupon= Coupon.builder()
                    .code(couponDTO.getCode())
                    .description(couponDTO.getDescription())
                    .active(true)
                    .employee(existsEmployee)
                    .build();
            couponRepository.save(newCoupon);
        }


        return newCoupon;
    }

    @Override
    public Coupon update(int id, CouponDTO couponDTO) throws DataNotFoundException {

        Employee existsEmployee = employeeService.getEmployeeById(couponDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + couponDTO.getEmployeeId()));
        Coupon existsCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        boolean existsCode = couponRepository.existsByCode(couponDTO.getCode());

        if(!existsCode){

            existsCoupon.setCode(couponDTO.getCode());
            existsCoupon.setDescription(couponDTO.getDescription());
            existsCoupon.setActive(couponDTO.isActive());
            existsCoupon.setEmployee(existsEmployee);
            couponRepository.save(existsCoupon);
        }
        return existsCoupon;
    }

    @Override
    public Optional<Coupon> getById(int id) throws DataNotFoundException {
        Coupon existsCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        return Optional.ofNullable(existsCoupon);
    }

    @Override
    public List<Coupon> getAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    public List<Coupon> searchCouponsNative(String keyword, BigDecimal minimumOrderValue, Date expirationDate) {
        return couponRepository.searchCouponsNative(keyword,minimumOrderValue,expirationDate);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);
        optionalCoupon.ifPresent(couponRepository :: delete);
    }
}
