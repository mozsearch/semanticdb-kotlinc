package com.sourcegraph.semanticdb_kotlinc

inline fun Semanticdb.TextDocuments.Builder.addDocuments(
    block:
    Semanticdb.TextDocument.Builder.() -> Unit
): Semanticdb.TextDocuments.Builder =
    this.addDocuments(Semanticdb.TextDocument.newBuilder().apply(block).build())

inline fun Semanticdb.TextDocument.Builder.addSymbols(
    block:
    Semanticdb.SymbolInformation.Builder.() -> Unit
): Semanticdb.TextDocument.Builder =
    this.addSymbols(Semanticdb.SymbolInformation.newBuilder().apply(block).build())

inline fun Semanticdb.TextDocument.Builder.addOccurrences(
    block:
    Semanticdb.SymbolOccurrence.Builder.() -> Unit
): Semanticdb.TextDocument.Builder =
    this.addOccurrences(Semanticdb.SymbolOccurrence.newBuilder().apply(block).build())

inline fun Semanticdb.Signature.Builder.classSignature(
    block:
    Semanticdb.ClassSignature.Builder.() -> Unit
): Semanticdb.Signature.Builder =
    this.setClassSignature(Semanticdb.ClassSignature.newBuilder().apply(block).build())

inline fun Semanticdb.Signature.Builder.methodSignature(
    block:
    Semanticdb.MethodSignature.Builder.() -> Unit
): Semanticdb.Signature.Builder =
    this.setMethodSignature(Semanticdb.MethodSignature.newBuilder().apply(block).build())

inline fun Semanticdb.Signature.Builder.typeSignature(
    block: Semanticdb.TypeSignature.Builder.() ->
    Unit
): Semanticdb.Signature.Builder =
    this.setTypeSignature(Semanticdb.TypeSignature.newBuilder().apply(block).build())

inline fun Semanticdb.Signature.Builder.valueSignature(
    block:
    Semanticdb.ValueSignature.Builder.() -> Unit
): Semanticdb.Signature.Builder =
    this.setValueSignature(Semanticdb.ValueSignature.newBuilder().apply(block).build())

inline fun Semanticdb.ClassSignature.Builder.typeParameters(
    block: Semanticdb.Scope.Builder.() ->
    Unit
): Semanticdb.ClassSignature.Builder =
    this.setTypeParameters(Semanticdb.Scope.newBuilder().apply(block).build())

inline fun Semanticdb.ClassSignature.Builder.addParents(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.ClassSignature.Builder =
    this.addParents(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.ClassSignature.Builder.declarations(
    block: Semanticdb.Scope.Builder.() ->
    Unit
): Semanticdb.ClassSignature.Builder =
    this.setDeclarations(Semanticdb.Scope.newBuilder().apply(block).build())

inline fun Semanticdb.MethodSignature.Builder.typeParameters(
    block: Semanticdb.Scope.Builder.() ->
    Unit
): Semanticdb.MethodSignature.Builder =
    this.setTypeParameters(Semanticdb.Scope.newBuilder().apply(block).build())

inline fun Semanticdb.MethodSignature.Builder.addParameterLists(
    block:
    Semanticdb.Scope.Builder.() -> Unit
): Semanticdb.MethodSignature.Builder =
    this.addParameterLists(Semanticdb.Scope.newBuilder().apply(block).build())

inline fun Semanticdb.MethodSignature.Builder.returnType(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.MethodSignature.Builder =
    this.setReturnType(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.TypeSignature.Builder.typeParameters(
    block: Semanticdb.Scope.Builder.() ->
    Unit
): Semanticdb.TypeSignature.Builder =
    this.setTypeParameters(Semanticdb.Scope.newBuilder().apply(block).build())

inline fun Semanticdb.TypeSignature.Builder.lowerBound(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.TypeSignature.Builder =
    this.setLowerBound(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.TypeSignature.Builder.upperBound(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.TypeSignature.Builder =
    this.setUpperBound(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.ValueSignature.Builder.tpe(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.ValueSignature.Builder =
    this.setTpe(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.SymbolInformation.Builder.signature(
    block: Semanticdb.Signature.Builder.() ->
    Unit
): Semanticdb.SymbolInformation.Builder =
    this.setSignature(Semanticdb.Signature.newBuilder().apply(block).build())

inline fun Semanticdb.SymbolInformation.Builder.access(block: Semanticdb.Access.Builder.() -> Unit):
        Semanticdb.SymbolInformation.Builder =
    this.setAccess(Semanticdb.Access.newBuilder().apply(block).build())

inline fun Semanticdb.SymbolInformation.Builder.documentation(
    block:
    Semanticdb.Documentation.Builder.() -> Unit
): Semanticdb.SymbolInformation.Builder =
    this.setDocumentation(Semanticdb.Documentation.newBuilder().apply(block).build())

inline fun Semanticdb.Access.Builder.privateAccess(
    block: Semanticdb.PrivateAccess.Builder.() ->
    Unit
): Semanticdb.Access.Builder =
    this.setPrivateAccess(Semanticdb.PrivateAccess.newBuilder().apply(block).build())

inline fun Semanticdb.Access.Builder.privateWithinAccess(
    block:
    Semanticdb.PrivateWithinAccess.Builder.() -> Unit
): Semanticdb.Access.Builder =
    this.setPrivateWithinAccess(Semanticdb.PrivateWithinAccess.newBuilder().apply(block).build())

inline fun Semanticdb.Access.Builder.protectedAccess(
    block: Semanticdb.ProtectedAccess.Builder.() ->
    Unit
): Semanticdb.Access.Builder =
    this.setProtectedAccess(Semanticdb.ProtectedAccess.newBuilder().apply(block).build())

inline fun Semanticdb.Access.Builder.publicAccess(
    block: Semanticdb.PublicAccess.Builder.() ->
    Unit
): Semanticdb.Access.Builder =
    this.setPublicAccess(Semanticdb.PublicAccess.newBuilder().apply(block).build())

inline fun Semanticdb.SymbolOccurrence.Builder.range(block: Semanticdb.Range.Builder.() -> Unit):
        Semanticdb.SymbolOccurrence.Builder =
    this.setRange(Semanticdb.Range.newBuilder().apply(block).build())

inline fun Semanticdb.Scope.Builder.addHardlinks(
    block: Semanticdb.SymbolInformation.Builder.() ->
    Unit
): Semanticdb.Scope.Builder =
    this.addHardlinks(Semanticdb.SymbolInformation.newBuilder().apply(block).build())

inline fun Semanticdb.Type.Builder.typeRef(block: Semanticdb.TypeRef.Builder.() -> Unit):
        Semanticdb.Type.Builder =
    this.setTypeRef(Semanticdb.TypeRef.newBuilder().apply(block).build())

inline fun Semanticdb.Type.Builder.existentialType(
    block: Semanticdb.ExistentialType.Builder.() ->
    Unit
): Semanticdb.Type.Builder =
    this.setExistentialType(Semanticdb.ExistentialType.newBuilder().apply(block).build())

inline fun Semanticdb.Type.Builder.intersectionType(
    block: Semanticdb.IntersectionType.Builder.() ->
    Unit
): Semanticdb.Type.Builder =
    this.setIntersectionType(Semanticdb.IntersectionType.newBuilder().apply(block).build())

inline fun Semanticdb.TypeRef.Builder.addTypeArguments(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.TypeRef.Builder =
    this.addTypeArguments(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.IntersectionType.Builder.addTypes(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.IntersectionType.Builder =
    this.addTypes(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.ExistentialType.Builder.tpe(block: Semanticdb.Type.Builder.() -> Unit):
        Semanticdb.ExistentialType.Builder =
    this.setTpe(Semanticdb.Type.newBuilder().apply(block).build())

inline fun Semanticdb.ExistentialType.Builder.declarations(
    block: Semanticdb.Scope.Builder.() ->
    Unit
): Semanticdb.ExistentialType.Builder =
    this.setDeclarations(Semanticdb.Scope.newBuilder().apply(block).build())

inline fun TextDocuments(block: Semanticdb.TextDocuments.Builder.() -> Unit):
        Semanticdb.TextDocuments = Semanticdb.TextDocuments.newBuilder().apply(block).build()

inline fun TextDocument(block: Semanticdb.TextDocument.Builder.() -> Unit): Semanticdb.TextDocument =
    Semanticdb.TextDocument.newBuilder().apply(block).build()

inline fun Range(block: Semanticdb.Range.Builder.() -> Unit): Semanticdb.Range =
    Semanticdb.Range.newBuilder().apply(block).build()

inline fun Signature(block: Semanticdb.Signature.Builder.() -> Unit): Semanticdb.Signature =
    Semanticdb.Signature.newBuilder().apply(block).build()

inline fun ClassSignature(block: Semanticdb.ClassSignature.Builder.() -> Unit):
        Semanticdb.ClassSignature = Semanticdb.ClassSignature.newBuilder().apply(block).build()

inline fun MethodSignature(block: Semanticdb.MethodSignature.Builder.() -> Unit):
        Semanticdb.MethodSignature = Semanticdb.MethodSignature.newBuilder().apply(block).build()

inline fun TypeSignature(block: Semanticdb.TypeSignature.Builder.() -> Unit):
        Semanticdb.TypeSignature = Semanticdb.TypeSignature.newBuilder().apply(block).build()

inline fun ValueSignature(block: Semanticdb.ValueSignature.Builder.() -> Unit):
        Semanticdb.ValueSignature = Semanticdb.ValueSignature.newBuilder().apply(block).build()

inline fun SymbolInformation(block: Semanticdb.SymbolInformation.Builder.() -> Unit):
        Semanticdb.SymbolInformation =
    Semanticdb.SymbolInformation.newBuilder().apply(block).build()

inline fun Access(block: Semanticdb.Access.Builder.() -> Unit): Semanticdb.Access =
    Semanticdb.Access.newBuilder().apply(block).build()

inline fun PrivateAccess(block: Semanticdb.PrivateAccess.Builder.() -> Unit):
        Semanticdb.PrivateAccess = Semanticdb.PrivateAccess.newBuilder().apply(block).build()

inline fun PrivateWithinAccess(block: Semanticdb.PrivateWithinAccess.Builder.() -> Unit):
        Semanticdb.PrivateWithinAccess =
    Semanticdb.PrivateWithinAccess.newBuilder().apply(block).build()

inline fun ProtectedAccess(block: Semanticdb.ProtectedAccess.Builder.() -> Unit):
        Semanticdb.ProtectedAccess = Semanticdb.ProtectedAccess.newBuilder().apply(block).build()

inline fun PublicAccess(block: Semanticdb.PublicAccess.Builder.() -> Unit): Semanticdb.PublicAccess =
    Semanticdb.PublicAccess.newBuilder().apply(block).build()

inline fun Documentation(block: Semanticdb.Documentation.Builder.() -> Unit):
        Semanticdb.Documentation = Semanticdb.Documentation.newBuilder().apply(block).build()

inline fun SymbolOccurrence(block: Semanticdb.SymbolOccurrence.Builder.() -> Unit):
        Semanticdb.SymbolOccurrence = Semanticdb.SymbolOccurrence.newBuilder().apply(block).build()

inline fun Scope(block: Semanticdb.Scope.Builder.() -> Unit): Semanticdb.Scope =
    Semanticdb.Scope.newBuilder().apply(block).build()

inline fun Type(block: Semanticdb.Type.Builder.() -> Unit): Semanticdb.Type =
    Semanticdb.Type.newBuilder().apply(block).build()

inline fun TypeRef(block: Semanticdb.TypeRef.Builder.() -> Unit): Semanticdb.TypeRef =
    Semanticdb.TypeRef.newBuilder().apply(block).build()

inline fun IntersectionType(block: Semanticdb.IntersectionType.Builder.() -> Unit):
        Semanticdb.IntersectionType = Semanticdb.IntersectionType.newBuilder().apply(block).build()

inline fun ExistentialType(block: Semanticdb.ExistentialType.Builder.() -> Unit):
        Semanticdb.ExistentialType = Semanticdb.ExistentialType.newBuilder().apply(block).build()
