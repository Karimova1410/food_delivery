package kg.alatoo.food_delivery.oauth;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.Role;
import kg.alatoo.food_delivery.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  public CustomOAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String provider = userRequest.getClientRegistration().getRegistrationId();
    String email = getEmailFromAttributes(provider, attributes);
    String name = getNameFromAttributes(provider, attributes);

    if (email == null || email.isEmpty()) {
      throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
    }

    if (name == null || name.isEmpty()) {
      name = email;
    }

    Optional<User> userOptional = userRepository.findByEmail(email);
    User user;

    if (userOptional.isEmpty()) {
      user = new User();
      user.setEmail(email);
      user.setName(name);
      user.setRole(Role.CLIENT);
      user.setPassword("OAUTH2_USER");
      user = userRepository.save(user);
    } else {
      user = userOptional.get();
    }

    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

    String nameAttributeKey = determineNameAttributeKey(provider);

    if (!attributes.containsKey(nameAttributeKey)) {
      attributes.put(nameAttributeKey, name);
    }
    System.out.println("OAuth2 attributes: " + attributes);

    return new DefaultOAuth2User(
        Collections.singletonList(authority),
        attributes,
        nameAttributeKey
    );
  }

  private String getEmailFromAttributes(String provider, Map<String, Object> attributes) {
    if ("google".equals(provider)) {
      return (String) attributes.get("email");
    } else if ("github".equals(provider)) {
      String email = (String) attributes.get("email");
      if (email == null) {
        email = (String) attributes.get("login") + "@github.com";
      }
      return email;
    }
    throw new OAuth2AuthenticationException("Unsupported OAuth2 provider");
  }

  private String getNameFromAttributes(String provider, Map<String, Object> attributes) {
    if ("google".equals(provider)) {
      return (String) attributes.get("name");
    } else if ("github".equals(provider)) {
      String name = (String) attributes.get("name");
      if (name == null) {
        name = (String) attributes.get("login");
      }
      return name;
    }
    return "Unknown";
  }

  private String determineNameAttributeKey(String provider) {
    if ("google".equals(provider)) {
      return "sub";
    } else if ("github".equals(provider)) {
      return "id";
    }
    return "email";
  }
}