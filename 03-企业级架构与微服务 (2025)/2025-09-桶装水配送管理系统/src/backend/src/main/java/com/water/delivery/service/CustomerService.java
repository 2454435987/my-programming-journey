package com.water.delivery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.water.delivery.dto.CustomerDTO;
import com.water.delivery.dto.CustomerQueryDTO;
import com.water.delivery.entity.Customer;
import com.water.delivery.vo.CustomerVO;
import com.water.delivery.vo.TicketAccountVO;
import java.util.List;

public interface CustomerService extends IService<Customer> {
    Page<CustomerVO> queryPage(CustomerQueryDTO queryDTO);
    CustomerVO getCustomerDetail(Long id);
    List<TicketAccountVO> getCustomerTickets(Long customerId);
    List<CustomerVO> searchCustomer(String keyword);
    boolean saveOrUpdateCustomer(CustomerDTO dto);
}
