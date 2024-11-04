package com.example.SpringAppGB.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Инициализатор для настройки DispatcherServlet Spring.
 * Расширяет AbstractAnnotationConfigDispatcherServletInitializer для настройки контекста приложения.
 */
public class MyDispatcherServletInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Возвращает конфигурационные классы корневого контекста приложения.
     *
     * @return массив классов конфигурации корневого контекста
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    /**
     * Возвращает конфигурационные классы для DispatcherServlet.
     *
     * @return массив классов конфигурации для DispatcherServlet
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {AppSpringConfig.class};
    }

    /**
     * Возвращает маппинги URL для DispatcherServlet.
     *
     * @return массив строк с маппингами URL
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    /**
     * Выполняет действия при запуске сервлета.
     *
     * @param aServletContext контекст сервлета
     * @throws ServletException если произошла ошибка при инициализации
     */
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }

    /**
     * Регистрирует фильтр для обработки скрытых методов HTTP.
     *
     * @param aContext контекст сервлета
     */
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }
}
