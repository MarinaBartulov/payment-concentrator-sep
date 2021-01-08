package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.model.Role;

public interface RoleService {

    Role findByName(String name);
}
