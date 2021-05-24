package davis.learn.springboot.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

public class CusUserDetailsManager implements UserDetailsManager, GroupManager {

	@Override
	public List<String> findAllGroups() {
		return null;
	}

	@Override
	public List<String> findUsersInGroup(String s) {
		return null;
	}

	@Override
	public void createGroup(String s, List<GrantedAuthority> list) {

	}

	@Override
	public void deleteGroup(String s) {

	}

	@Override
	public void renameGroup(String s, String s1) {

	}

	@Override
	public void addUserToGroup(String s, String s1) {

	}

	@Override
	public void removeUserFromGroup(String s, String s1) {

	}

	@Override
	public List<GrantedAuthority> findGroupAuthorities(String s) {
		return null;
	}

	@Override
	public void addGroupAuthority(String s, GrantedAuthority grantedAuthority) {

	}

	@Override
	public void removeGroupAuthority(String s, GrantedAuthority grantedAuthority) {

	}

	@Override
	public void createUser(UserDetails userDetails) {

	}

	@Override
	public void updateUser(UserDetails userDetails) {

	}

	@Override
	public void deleteUser(String s) {

	}

	@Override
	public void changePassword(String s, String s1) {

	}

	@Override
	public boolean userExists(String s) {
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		return null;
	}
}