package judge.service.impl;

import judge.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import judge.model.entity.Role;
import judge.model.service.RoleServiceModel;
import judge.repository.RoleRepository;

import javax.annotation.PostConstruct;

import static judge.constant.Constants.ROLE_ADMIN;
import static judge.constant.Constants.ROLE_USER;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public RoleServiceImpl(RoleRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // run with bin initialisation
    @PostConstruct
    public void init(){
        if(this.repository.count() == 0) {
            this.repository.save(new Role(ROLE_ADMIN));
            this.repository.save(new Role(ROLE_USER));
        }
    }

    @Override
    public RoleServiceModel findByName(String name) {
        return this.repository
                .findByName(name)
                .map(role -> this.mapper.map(role, RoleServiceModel.class))
                .orElse(null);
    }
}
