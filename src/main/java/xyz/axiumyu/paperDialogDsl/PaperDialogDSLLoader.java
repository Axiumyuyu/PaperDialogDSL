package xyz.axiumyu.paperDialogDsl;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class PaperDialogDSLLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        // 声明 Kotlin 标准库依赖
        resolver.addDependency(
                new Dependency(new DefaultArtifact("org.jetbrains.kotlin:kotlin-stdlib:2.0.0"), null)
        );

        // 声明 Maven 中央仓库
        resolver.addRepository(
                new RemoteRepository.Builder("central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build()
        );

        classpathBuilder.addLibrary(resolver);
    }
}