package com.projekat.ris.report;

import com.projekat.ris.dto.OrderDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.service.OrderService;
import com.projekat.ris.service.UserService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderService orderService;
    private final UserService userService;

    public byte[] myOrdersPdf(Long userId) {
        // 1) Fetch data
        UserResponseDTO userInfo = userService.getUserById(userId);
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);

        List<Map<String, ?>> rows = new ArrayList<>();
        for (OrderDTO o : orders) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", o.getId());
            m.put("createdAt", Date.from(o.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
            m.put("status", o.getStatus().name());
            m.put("totalPrice", o.getTotalPrice());

            List<Map<String, ?>> itemRows = new ArrayList<>();
            if (o.getItems() != null) {
                o.getItems().forEach(it -> {
                    Map<String, Object> im = new HashMap<>();
                    im.put("productName", it.getProductName());
                    im.put("quantity", it.getQuantity());
                    im.put("price", it.getPrice());
                    im.put("lineTotal", it.getPrice() * it.getQuantity());
                    itemRows.add(im);
                });
            }
            m.put("items", itemRows);

            rows.add(m);
        }

        // 3) Parameters
        Map<String, Object> params = new HashMap<>();
        params.put("USERNAME", userInfo.getUsername());
        params.put("FIRST_NAME", userInfo.getFirstname());
        params.put("LAST_NAME", userInfo.getLastname());
        params.put("GENERATED_AT", new Date());

        // 4) Fill + export to PDF
        try (InputStream mainIn  = getClass().getResourceAsStream("/reports/my_orders.jrxml");
             InputStream itemsIn = getClass().getResourceAsStream("/reports/my_orders_items.jrxml")) {

            if (mainIn == null || itemsIn == null) {
                throw new IllegalStateException("Report template(s) not found under /reports");
            }

            JasperReport itemsReport = JasperCompileManager.compileReport(itemsIn);
            JasperReport mainReport  = JasperCompileManager.compileReport(mainIn);

            params.put("SUBREPORT", itemsReport);

            JRDataSource ds = new JRMapCollectionDataSource(rows);
            JasperPrint print = JasperFillManager.fillReport(mainReport, params, ds);
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate My Orders PDF", e);
        }
    }
}
