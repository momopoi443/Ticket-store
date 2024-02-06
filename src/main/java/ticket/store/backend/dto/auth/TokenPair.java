package ticket.store.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenPair {

    private String accessToken;

    private String refreshToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenPair tokenPair)) return false;

        if (getAccessToken() != null ? !getAccessToken().equals(tokenPair.getAccessToken()) : tokenPair.getAccessToken() != null)
            return false;
        return getRefreshToken() != null ? getRefreshToken().equals(tokenPair.getRefreshToken()) : tokenPair.getRefreshToken() == null;
    }

    @Override
    public int hashCode() {
        int result = getAccessToken() != null ? getAccessToken().hashCode() : 0;
        result = 31 * result + (getRefreshToken() != null ? getRefreshToken().hashCode() : 0);
        return result;
    }
}
