package judge.service;

import judge.model.service.RoleServiceModel;

public interface RoleService {
    RoleServiceModel findByName(String name);
}
