package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.model.Role;
import team16.paymentserviceprovider.repository.RoleRepository;
import team16.paymentserviceprovider.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }
}
