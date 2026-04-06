import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val generateIconCodes = tasks.register<GenerateIconCodesTask>("generateIconCodes") {
    iconFiles.from(
        fileTree("src/commonMain/kotlin/com/valentinbell/composeanimatedicons/icons") {
            include("**/*.kt")
        }
    )
    outputDirectory.set(layout.buildDirectory.dir("generated/icons"))
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generateIconCodes.map { it.outputDirectory })

            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.compose.material3.adaptive)
                implementation(libs.compose.material.icons.extended)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

abstract class GenerateIconCodesTask : DefaultTask() {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:SkipWhenEmpty
    abstract val iconFiles: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    private data class IconEntry(
        val id: String,
        val functionName: String,
        val label: String,
        val descriptionRes: String,
    )

    @TaskAction
    fun generate() {
        val outDir = outputDirectory.get().asFile.apply { mkdirs() }
        val codesFile = outDir.resolve("GeneratedIconCodes.kt")
        val registryFile = outDir.resolve("GeneratedIconRegistry.kt")

        val entries = mutableListOf<IconEntry>()

        val codesContent = buildString {
            appendLine("package icons")
            appendLine()
            appendLine("// AUTO-GENERATED — do not edit manually")
            appendLine("object GeneratedIconCodes {")

            iconFiles.files
                .filter { it.extension == "kt" }
                .sortedBy { it.name }
                .forEach { file ->
                    val id = convertToSnakeUpperCase(file.nameWithoutExtension)
                    val functionName = extractComposableName(file.readText())
                    val rawCode = extractComposableBody(file.readText())
                    val label = convertToSpaced(file.nameWithoutExtension)


                    entries += IconEntry(
                        id = id,
                        functionName = functionName ?: file.nameWithoutExtension,
                        label = label,
                        descriptionRes = "Res.string.icon_${id.lowercase()}_description",
                    )

                    appendLine()
                    appendLine("    const val $id = \"\"\"")
                    appendLine(rawCode)
                    appendLine("    \"\"\"")
                }

            appendLine("}")
        }

        codesFile.writeText(codesContent)

        val iconImports = entries.map { e ->
            "import com.valentinbell.composeanimatedicons.icons.${e.functionName}"
        }.distinct()

        val resourceImports = entries.map { e ->
            "import composeanimatedicons.composeapp.generated.resources.icon_${e.id.lowercase()}_description"
        }.distinct()

        val registryContent = buildString {
            appendLine("package icons")
            appendLine()
            appendLine("// AUTO-GENERATED — do not edit manually")
            appendLine("import androidx.compose.runtime.Composable")
            appendLine("import androidx.compose.ui.Modifier")
            appendLine("import androidx.compose.ui.graphics.Color")
            appendLine("import org.jetbrains.compose.resources.StringResource")
            appendLine()

            iconImports.forEach { appendLine(it) }
            appendLine()

            appendLine("import composeanimatedicons.composeapp.generated.resources.Res")
            resourceImports.forEach { appendLine(it) }
            appendLine()

            appendLine("enum class IconId {")
            entries.forEach { appendLine("    ${it.id},") }
            appendLine("}")
            appendLine()

            appendLine("data class IconMeta(")
            appendLine("    val id: IconId,")
            appendLine("    val label: String,")
            appendLine("    val descriptionRes: StringResource")
            appendLine(")")
            appendLine()

            appendLine("@Composable")
            appendLine("fun renderIcon(id: IconId, tint: Color, modifier: Modifier = Modifier, animate: Boolean = true) {")
            appendLine("    when (id) {")
            entries.forEach { e ->
                appendLine("        IconId.${e.id} -> ${e.functionName}(animate = animate, tint = tint, modifier = modifier)")
            }
            appendLine("    }")
            appendLine("}")
            appendLine()

            appendLine("fun iconCode(id: IconId): String {")
            appendLine("    return when (id) {")
            entries.forEach { e ->
                appendLine("        IconId.${e.id} -> GeneratedIconCodes.${e.id}")
            }
            appendLine("    }")
            appendLine("}")
            appendLine()

            appendLine("val iconRegistry: List<IconMeta> = listOf(")
            entries.forEach { e ->
                appendLine("    IconMeta(IconId.${e.id}, \"${e.label}\", ${e.descriptionRes}),")
            }
            appendLine(")")
        }

        registryFile.writeText(registryContent)
    }

    private fun extractComposableName(content: String): String? {
        val regex = Regex("""fun\s+([A-Za-z][A-Za-z0-9_]*)\s*\(""")
        val composableIdx = content.indexOf("@Composable")
        if (composableIdx < 0) return null
        return regex.find(content, composableIdx)?.groupValues?.get(1)
    }

    private fun convertToSnakeUpperCase(name: String) =
        name.replace(Regex("([a-z])([A-Z])"), "$1_$2").uppercase()

    private fun convertToSpaced(name: String) =
        name.replace(Regex("([a-z])([A-Z])"), "$1 $2")

    private fun extractComposableBody(content: String): String {
        val startIndex = content.indexOf("@Composable")
        return if (startIndex >= 0) content.substring(startIndex) else content
            .trimIndent()
            .replace("$", "\${'$'}")
            .replace("\"\"\"", "\"\"${'\"'}")
    }
}

tasks.named("generateComposeResClass") {
    dependsOn(generateIconCodes)
}