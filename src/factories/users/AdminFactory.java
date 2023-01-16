package factories.users;

import models.users.Admin;
import models.users.User;

public class AdminFactory implements UserFactory {

	@Override
	public User getUser() {
		return new Admin();
	}

}
