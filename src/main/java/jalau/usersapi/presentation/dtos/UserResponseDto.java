package jalau.usersapi.presentation.dtos;

public class UserResponseDto {
    private String id;
    private String name;
    private String login;
    public UserResponseDto() {}

    public UserResponseDto(String id, String name, String login) {
        this.id = id;
        this.name = name;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}