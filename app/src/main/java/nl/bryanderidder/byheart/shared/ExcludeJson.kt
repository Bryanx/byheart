package nl.bryanderidder.byheart.shared

/**
 * Custom annotation for excluding certain fields from being serialized.
 * @author Bryan de Ridder
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ExcludeJson