package net.stargraph.core.qa.nli;

import net.stargraph.Language;
import net.stargraph.core.qa.Rules;
import net.stargraph.core.qa.annotator.Annotator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.List;
import java.util.Objects;

public final class QuestionAnalyzer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Marker marker = MarkerFactory.getMarker("nli");

    private Language language;
    private Annotator annotator;
    private List<DataModelTypePattern> dataModelTypePatterns;
    private List<QueryPlanPattern> queryPlanPatterns;

    public QuestionAnalyzer(Language language, Annotator annotator, Rules rules) {
        this.language = Objects.requireNonNull(language);
        this.annotator = Objects.requireNonNull(annotator);
        this.dataModelTypePatterns = rules.getDataModelTypeRules(language);
        this.queryPlanPatterns = rules.getQueryPlanRules(language);
    }

    public AnalyzedQuestion analyse(String question) {
        logger.info(marker, "Analyzing '{}'", Objects.requireNonNull(question));
        AnalyzedQuestion analyzed = new AnalyzedQuestion(language, question);
        analyzed.addAnnotations(annotator.run(language, question));
        resolveDataModel(analyzed);
        return analyzed;
    }

    private void resolveDataModel(AnalyzedQuestion analyzedQuestion) {
        dataModelTypePatterns.forEach(analyzedQuestion::transform);
    }
}