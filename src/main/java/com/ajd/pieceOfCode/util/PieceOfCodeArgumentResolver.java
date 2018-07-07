package com.ajd.pieceOfCode.util;

import com.ajd.pieceOfCode.pieceOfCode.PieceOfCode;
import com.ajd.pieceOfCode.pieceOfCode.PieceOfCodeRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.TagUtils;

import java.util.Map;
import java.util.Optional;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Component
@Scope(value = TagUtils.SCOPE_REQUEST, proxyMode = TARGET_CLASS)
public class PieceOfCodeArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String PIECE_OF_CODE_ID = "pieceOfCodeId";
    private final PieceOfCodeRepository pieceOfCodeRepository;
    private Optional<PieceOfCode> requestedPieceOfCode = Optional.empty();

    @Autowired
    public PieceOfCodeArgumentResolver(PieceOfCodeRepository pieceOfCodeRepository) {
        this.pieceOfCodeRepository = pieceOfCodeRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PieceOfCode.class);
    }

    @Override
    public PieceOfCode resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (requestedPieceOfCode.isPresent()) {
            return requestedPieceOfCode.get();
        }
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (!pathVariables.containsKey(PIECE_OF_CODE_ID)) {
            throw new IllegalArgumentException(
                    "A path parameter named '{" + PIECE_OF_CODE_ID + "}' is required to be able to resolve a PieceOfCode argument in  controller.");
        }
        Long pieceOfCodeId = Long.valueOf(pathVariables.get(PIECE_OF_CODE_ID));
        Optional<PieceOfCode> pieceOfCodeOptionallyResolved = pieceOfCodeRepository.findById(pieceOfCodeId);
        PieceOfCode resolvedPieceOfCode =  pieceOfCodeOptionallyResolved.orElseThrow(() ->
                new NotFoundException("The piece of code with the id " + pieceOfCodeId + " does not exist"));
        requestedPieceOfCode = Optional.of(resolvedPieceOfCode);
        return resolvedPieceOfCode;
    }
}