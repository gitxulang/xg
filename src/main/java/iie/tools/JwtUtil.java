package iie.tools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
 
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
public class JwtUtil {
    static final String SECRET = "IIE_nms_zyj";
 
    public static String generateToken(String user) {
        HashMap<String, Object> map = new HashMap<>();
        // you can put any data in the map
        map.put("username", user);
        String jwt = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000L)) // 24 hour 86400000 ms
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return "Bearer " + jwt; //JWT前面一般会加Bearer
    }
 
    public static boolean validateToken(String token, String sessionToken) {
        try {
        	if (token == null || token.equals("false") || !token.contains("Bearer ")) {
        		System.out.println("[DEBUG] validateToken false in JwtUtil, token illegal: " + token);
        		return false;
        	}
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            for (Object value : body.values()) {
                if (value.equals(sessionToken)) {
                	return true;
                }
            }
            System.out.println("[DEBUG] validateToken false in JwtUtil, token timeout: " + token);
            return false;
        } catch (Exception e) {
        	System.out.println("[ERROR] validateToken false: " + e.toString());
            return false;
        }
    }
}
