package org.arispay.test.infrastructure.adapters.http;

import okhttp3.*;
import org.arispay.adapters.fbl.httpclient.TsqAdapter;
import org.arispay.data.TransactionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Disabled;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled("Ignore this test as it requires network access")
public class TsqAdapterTest {
    @Test
    void testQueryTransaction_MockedResponse() throws Exception {
        OkHttpClient mockClient = mock(OkHttpClient.class);
        Call mockCall = mock(Call.class);
        ResponseBody mockBody = ResponseBody.create(
                "{\"id\": \"123\", \"tranAmount\": 100.0}", MediaType.get("application/json"));
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(mockBody)
                .build();

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        TsqAdapter adapter = new TsqAdapter(mockClient);
        TransactionDto dto = adapter.queryTransaction("BANKREF123");

        assertNotNull(dto);
        assertEquals(123L, dto.getId());
        assertEquals(100.0, dto.getTranAmount());
    }
}
