package xyz.axiumyu.playerDisplay

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.PluginLoader
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.RemoteRepository

internal class PlayerDisplayLoader : PluginLoader {

    override fun classloader(builder: PluginClasspathBuilder) {
        val resolver = MavenLibraryResolver()

        resolver.addDependency(
            Dependency(DefaultArtifact("org.jetbrains.kotlin:kotlin-stdlib:2.4.0"), null)
        )

        resolver.addRepository(
            RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2/").build()
        )

        builder.addLibrary(resolver)
    }
}
