package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.comm.CustomExtractableResponse;
import nextstep.subway.domain.Line;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
	private static final String BASIC_URL_LINES = "/lines";

	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() {
		if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
			RestAssured.port = port;
		}
	}

	/**
     * When 노선을 생성하면
     * Then 노선이 생성된다
     * Then 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> CreateResponse = 노선_생성_요청("1호선", "파랑색");
        Line createdLine =  CustomExtractableResponse.getObject(CreateResponse, Line.class);
        List<Line> lines = 노선_리스트_조회(); 

		// then
		assertAll(() -> assertThat(CreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
				() -> assertNotNull(createdLine.getId()),
				() -> assertEquals(createdLine, lines.get(0)));
    }   
    
    /**
     * Given 2개의 노선을 생성하고
     * When 노선 목록을 조회하면
     * Then 2개의 노선을 응답 받는다
     */
    @DisplayName("노선을 조회한다.")
    @Test
    void getLines() {
        // given
    	노선_생성_요청("1호선", "파랑색");
    	노선_생성_요청("2호선", "초록색");
		
		// when
        ExtractableResponse<Response> response = CustomExtractableResponse.get(BASIC_URL_LINES);
        List<Line> lines = 노선_리스트_조회();

		// then
		assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
				() -> assertThat(lines).hasSize(2));
    }    
    
    /**
     * Given 2개의 노선을 생성하고
     * When id값으로 노선을 조회하면
     * Then 원하는 노선을 응답 받는다.
     */
    @DisplayName("노선id로 원하는 노선을 조회한다.")
    @Test
    void getLine() {
        // given
    	노선_생성_요청("1호선", "파랑색");
    	Line createLine = CustomExtractableResponse
				.getObject(노선_생성_요청("2호선", "초록색"), Line.class);
		
        // when
        Line line = 노선_ID_조회(createLine.getId());

		// then
        assertEquals(createLine, line);
    }    

    /**
     * Given 2개의 노선을 생성하고
     * When 생성한 지하철 노선을 수정
     * Then 해당 지하철 노선 정보는 수정 확인
     */
    @DisplayName("원하는 노선의 정보를 수정한다")
    @Test
    void updateLine() {
        // given
    	노선_생성_요청("1호선", "파랑색");
    	Line createLine = CustomExtractableResponse.getObject(노선_생성_요청("2호선", "초록색"), Line.class);
		
        // when
		ExtractableResponse<Response> response = 노선_수정_요청(createLine, "3호선", "주황색");
		Line line = CustomExtractableResponse.getObject(response, Line.class);

		// then
		assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
				() -> assertEquals(createLine, line),
				() -> assertFalse(createLine.match(line)));
    }
    
    /**
     * Given 노선을 생성하고
     * When 생성한 지하철 노선을 삭제
     * Then 해당 지하철 노선 정보는 수정 확인
     */
    @DisplayName("원하는 노선의 정보를 삭제한다")
    @Test
    void deleteLine() {
        // given
    	Line createLine = CustomExtractableResponse.getObject(노선_생성_요청("1호선", "파랑색"), Line.class);
		
        // when
		ExtractableResponse<Response> response = 노선_삭제_요청(createLine);

		// then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

	private List<Line> 노선_리스트_조회() {
		return CustomExtractableResponse.getList(CustomExtractableResponse.get(BASIC_URL_LINES), Line.class);
	}

	private Line 노선_ID_조회(Long id) {
		String url = CustomExtractableResponse.joinUrl(BASIC_URL_LINES, id);
		return CustomExtractableResponse.getObject(CustomExtractableResponse.get(url), Line.class);
	}

	private ExtractableResponse<Response> 노선_생성_요청(String name, String color) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		return CustomExtractableResponse.post(BASIC_URL_LINES, params);
	}

	private ExtractableResponse<Response> 노선_수정_요청(Line createLine, String name, String color) {
		Map<String, String> params = new HashMap<>();
		params.put("name", "3호선");
		params.put("color", "주황색");

		String url = CustomExtractableResponse.joinUrl(BASIC_URL_LINES, createLine.getId());
		return CustomExtractableResponse.put(url, params);
	}

	private ExtractableResponse<Response> 노선_삭제_요청(Line createLine) {
		String url = CustomExtractableResponse.joinUrl(BASIC_URL_LINES, createLine.getId());
		return CustomExtractableResponse.delete(url);
	}
}
