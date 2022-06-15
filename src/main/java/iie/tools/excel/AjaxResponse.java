package iie.tools.excel;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * @author MrBird
 */
public class AjaxResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public AjaxResponse code(HttpStatus status) {
        this.put("code", status.value());
        return this;
    }

    public AjaxResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public AjaxResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    public AjaxResponse success() {
        this.code(HttpStatus.OK);
        return this;
    }

    public AjaxResponse fail() {
        this.code(HttpStatus.INTERNAL_SERVER_ERROR);
        return this;
    }

    @Override
    public AjaxResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
