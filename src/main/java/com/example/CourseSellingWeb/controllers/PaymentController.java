package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.configurations.VnPayConfig;
import com.example.CourseSellingWeb.dtos.PaymentDTO;
import com.example.CourseSellingWeb.dtos.TransactionStatusDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Order;
import com.example.CourseSellingWeb.respositories.OrderRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderRepository orderRepository;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(
            HttpServletRequest request,
            @RequestParam(value = "final_price") BigDecimal amount,
            @RequestParam(value = "order_id") Integer orderId
    ) throws UnsupportedEncodingException {
        String orderType = "other";
//        Integer orderId = 1;
//        amount = amount*2400000;
         amount = amount.multiply(BigDecimal.valueOf(100));
//            String bankCode = req.getParameter("bankCode");;

        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
//            String vnp_IpAddr = VnPayConfig.getIpAddress(req);

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderId.toString());
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl+orderId+"?order_id="+orderId);
        vnp_Params.put("vnp_IpAddr", VnPayConfig.getIpAddress(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .status("OK")
                .message(localizationUtils.getLocalizationMessage(MessageKeys.PAYMENT_SUCCESSFULLY))
                .url(paymentUrl)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }
    @GetMapping("/check-payment")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "order_id") Integer orderId,
            @RequestParam(value = "vnp_Amount") Long amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_CardType", required = false) String cardType,
            @RequestParam(value = "vnp_OrderInfo") String orderInfo,
            @RequestParam(value = "vnp_PayDate") String payDate,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_ResponseCode") String tmnCode,
            @RequestParam(value = "vnp_TransactionNo") String transactionNo,
            @RequestParam(value = "vnp_TransactionStatus") String transactionStatus,
            @RequestParam(value = "vnp_TxnRef") String txnRef,
            @RequestParam(value = "vnp_SecureHash") String secureHash
    ) throws DataNotFoundException {
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
        if (responseCode.equals("00")){
            transactionStatusDTO = TransactionStatusDTO.builder()
                    .status("Accepted")
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.PAYMENT_SUCCESSFULLY))
                    .data("http://localhost:3000/order-confirm/"+orderId)
                    .build();

            return ResponseEntity.ok(transactionStatusDTO);
        }
        else
        {
            Order order = this.orderRepository.findById(orderId)
                    .orElseThrow(() -> new DataNotFoundException(
                          MessageKeys.NOT_FOUND + orderId
                    ));
            order.setPaymentMethod("other");
            order.setActive(false);
            this.orderRepository.save(order);
            return ResponseEntity.badRequest().body(TransactionStatusDTO.builder()
//                    .status()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.PAYMENT_FAILED))
                    .data("")
                    .build());
        }
    }
}
